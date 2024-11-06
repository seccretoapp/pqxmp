package com.seccreto.infrastructure.api;

import com.seccreto.application.account.retrieve.get.GetAccountByIdUseCase;
import com.seccreto.exceptions.DomainException;
import com.seccreto.exceptions.ErrorResponse;
import com.seccreto.exceptions.NotFoundException;
import com.seccreto.infrastructure.account.presenter.AccountApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
public class AccontController implements AccountAPI {

  private final GetAccountByIdUseCase getAccountByIdUseCase;

  public AccontController(final GetAccountByIdUseCase getAccountByIdUseCase) {
    this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
  }

  @Override
  public ResponseEntity<?> getById(String id) {
    try {
      final var account = AccountApiPresenter.present(getAccountByIdUseCase.execute(id));
      return new ResponseEntity<>(account, HttpStatus.OK);
    } catch (NotFoundException error) {
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setMessage(error.getMessage());
      errorResponse.setTimestamp(LocalDateTime.now().toString());
      errorResponse.setError("Not Found");
      errorResponse.setPath("/api/account/" + id);
      errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    } catch (Exception error) {
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setMessage(error.getMessage());
      errorResponse.setTimestamp(LocalDateTime.now().toString());
      errorResponse.setError("Internal Server Error");
      errorResponse.setPath("/api/account/" + id);
      errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
