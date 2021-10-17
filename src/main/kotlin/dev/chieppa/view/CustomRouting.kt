package dev.chieppa.view

import dev.chieppa.config.config
import dev.chieppa.config.plugins.UserSession
import dev.chieppa.config.plugins.jsonFormat
import dev.chieppa.controller.*
import dev.chieppa.model.exception.ExceptionType
import dev.chieppa.model.recievables.Login
import dev.chieppa.model.response.ErrorResponse
import dev.chieppa.model.response.InformationalResponse
import dev.chieppa.model.response.SuccessResponse
import dev.chieppa.util.CREATE_ACCOUNT
import dev.chieppa.util.HOME
import dev.chieppa.util.LOGIN
import dev.chieppa.util.LOGOUT
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.thymeleaf.*
import kotlinx.serialization.json.encodeToJsonElement
import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

fun Application.createHomeRoute() {
    routing {
        get(HOME) {
            if (call.sessions.get<UserSession>() != null) {
                call.respond("User successfully logged in")
            } else {
                logger.info { "Connection from ${call.request.local.host}" }
                call.respond(
                    ThymeleafContent(
                        "homepage",
                        mapOf("new_accounts_enabled" to config.users.enableNewAccounts)
                    )
                )
            }
        }
    }
}

fun Application.createUserRoute() {
    routing {
        post(CREATE_ACCOUNT) {
            val user = call.receive<Login>()

            if (userExits(user.username.orEmpty())) {
                call.respond(
                    ErrorResponse(
                        errorType = ExceptionType.ACCOUNT_EXISTS,
                        error = jsonFormat.encodeToJsonElement(
                            InformationalResponse(
                                message = "The account could not be created because an account with this username already exists"
                            )
                        )
                    )
                )
                return@post
            }

            if (createUser(user.username.orEmpty(), user.password.orEmpty())) {
                call.sessions.set(createSession(user.username.orEmpty()))
                call.respond(SuccessResponse())
            } else {
                call.respond(
                    ErrorResponse(
                        errorType = ExceptionType.ACCOUNT_NOT_CREATED,
                        error = jsonFormat.encodeToJsonElement(InformationalResponse(message = "Account could not be created"))
                    )
                )
            }
        }
    }
}

fun Application.createLoginRoute() {
    routing {
        post(LOGIN) {
            val user = call.receive<Login>()
            if (validateLogin(user.username.orEmpty(), user.password.orEmpty())) {
                val session = createSession(user.username.orEmpty())
                call.sessions.set(session)
                call.respond(SuccessResponse())
            } else {
                call.respond(
                    ErrorResponse(
                        errorType = ExceptionType.ACCOUNT_DNE,
                        error = jsonFormat.encodeToJsonElement(InformationalResponse(message = "Account Does Not Exist"))
                    )
                )
            }
        }

        get(LOGOUT) {
            call.sessions.get<UserSession>()?.let { deleteSession(it) }
            call.sessions.clear<UserSession>()
            call.respondRedirect(HOME)
        }
    }
}

