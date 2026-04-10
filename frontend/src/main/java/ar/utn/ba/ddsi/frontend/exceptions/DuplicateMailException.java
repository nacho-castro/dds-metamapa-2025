package ar.utn.ba.ddsi.frontend.exceptions;

public class DuplicateMailException extends RuntimeException{

  public DuplicateMailException(String mail) {
      super("El mail " + mail + " ya existe");
  }
}
