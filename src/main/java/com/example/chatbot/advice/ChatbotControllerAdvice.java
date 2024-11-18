package com.example.chatbot.advice;


import com.example.chatbot.domain.ErrorResponse;
import com.example.chatbot.exception.NotFoundMemberException;
import com.example.chatbot.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ChatbotControllerAdvice {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(NullPointerException.class)
	public ErrorResponse serverException(NullPointerException nullPointerException) {

		log.error(DataUtil.makeErrorLogMessage(nullPointerException));
		return new ErrorResponse("chatbot error");
	}

	// properly not working 2024-11-18
	@ExceptionHandler(NotFoundMemberException.class)
	public void notFoundException(NotFoundMemberException notFoundMemberException) {
		log.error(DataUtil.makeErrorLogMessage(notFoundMemberException));
	}

}