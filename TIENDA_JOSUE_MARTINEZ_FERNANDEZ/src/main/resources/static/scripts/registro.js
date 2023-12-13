let formulario = document.getElementById("formulario");
let email = document.getElementById("email");
let nombre = document.getElementById("nombre");
let apellidos = document.getElementById("apellidos");
let pass = document.getElementById("pass");
let passTwo = document.getElementById("passTwo");

formulario.addEventListener("submit", function (event) {
  if (!validacionNombre(nombre.value) || !validacionApellidos(apellidos.value) ||
    !validacionPass(pass.value) || !validacionPassTwo(passTwo.value) ||
    !validacionPassIguales(pass.value, passTwo.value) || !validacionEmail(email.value)) {
    event.preventDefault();
  }
});

function validacionNombre(nombreValue) {
  if (nombreValue.trim() === "") {
    alert("El nombre no debe estar vacío");
    return false;
  }
  if (nombreValue.length < 1 || nombreValue.length > 50) {
    alert("El nombre debe tener entre 1 y 50 caracteres");
    return false;
  }
  return true;
}

function validacionApellidos(apellidosValue) {
  if (apellidosValue.trim() === "") {
    alert("Los apellidos no deben estar vacíos");
    return false;
  }
  if (apellidosValue.length < 1 || apellidosValue.length > 100) {
    alert("Los apellidos deben tener entre 1 y 100 caracteres");
    return false;
  }
  return true;
}

function validacionPass(passValue) {
  if (passValue.length < 8 || passValue.length > 16) {
    alert("La contraseña debe tener entre 8 y 16 caracteres");
    return false;
  }
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
  if (!regex.test(passValue)) {
    alert("La contraseña debe contener al menos una mayúscula, una minúscula, un número y tener entre 8 y 16 caracteres");
    return false;
  }
  return true;
}

function validacionPassTwo(passTwoValue) {
  if (passTwoValue.length < 8 || passTwoValue.length > 16) {
    alert("La contraseña dos debe tener entre 8 y 16 caracteres");
    return false;
  }
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
  if (!regex.test(passTwoValue)) {
    alert("La contraseña dos debe contener al menos una mayúscula, una minúscula, un número y tener entre 8 y 16 caracteres");
    return false;
  }
  return true;
}

function validacionPassIguales(passValue, passTwoValue) {
  if (passValue !== passTwoValue) {
    alert("Las contraseñas no coinciden");
    return false;
  }
  return true;
}

function validacionEmail(emailValue) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!regex.test(emailValue)) {
    alert("El email no tiene un formato válido");
    return false;
  }
  return true;
}

