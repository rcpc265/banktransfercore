package com.portfolio.banktransfercore.scratch;

// TU EXAMEN: Completa el código de la clase UnderInvestigationState.
// Reglas:
// 1. El estado no debe permitir débitos (validateDebit).
// 2. El estado SÍ debe permitir créditos embargados del gobierno (validateCredit).
// 3. Asume que la interfaz base 'AccountState' usa "Deny by Default" (lanza Exception) en ambos
// métodos.
// 4. Escribe el código exacto necesario. (Pista: si la interfaz base ya bloquea, ¿qué tienes que
// sobreescribir?).

public class UnderInvestigationState implements AccountState {

  @Override
  public void validateDebit(Money amount) {
    throw new UnsupportedOperationException("This account cannot debit");
    // espera esto de you must override lo pusiste por la pregunta que te hice si
    // por defecto debia rechazarr? tu respuesta es esta que por defecto exiga al
    // dev que lo implmente? respondeme eso
    // por cierto no desactivaste el autcompletado por ia, e estan consumiendo mis
    // tokens mientras escribo, no solo debes desactivar el atajo tambien tienes que
    // desasctivar el autocompetado maldita sea

    // no creo que deny by default consista en lanzar una ecepcion mmm pucha
    // tu test estaba faccil solo querias que ahga esto?

  }

  @Override
  public void validateCredit(Money amount) {}
}
