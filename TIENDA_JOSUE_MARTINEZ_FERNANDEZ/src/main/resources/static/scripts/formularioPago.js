metodoPago = document.getElementById("metodopago");
const metodoPagoValue = metodoPago.value;
const paypalemail = document.getElementById("paypalemail");
const paypalpassword = document.getElementById("paypalpass");
const tarjetanumero = document.getElementById("tarjetanumero");
const tarjetapropietario = document.getElementById("tarjetapropietario");
const tarjetacvv = document.getElementById("tarjetacvv");
const tarjetacaducidad = document.getElementById("tarjetacaducidad");
if (metodoPagoValue === "paypal") {
	paypalemail.parentNode.classList.remove("d-none");
	paypalpass.parentNode.classList.remove("d-none");
	tarjetanumero.parentNode.classList.add("d-none");
	tarjetapropietario.parentNode.classList.add("d-none");
	tarjetacvv.parentNode.classList.add("d-none");
	tarjetacaducidad.parentNode.classList.add("d-none");
} else if (metodoPagoValue === "tarjeta") {
	paypalemail.parentNode.classList.add("d-none");
	paypalpass.parentNode.classList.add("d-none");
	tarjetanumero.parentNode.classList.remove("d-none");
	tarjetapropietario.parentNode.classList.remove("d-none");
	tarjetacvv.parentNode.classList.remove("d-none");
	tarjetacaducidad.parentNode.classList.remove("d-none");
}

metodoPago.addEventListener('change', () => {
	const metodoPagoValue = metodoPago.value;
	const paypalemail = document.getElementById("paypalemail");
	const paypalpassword = document.getElementById("paypalpass");
	const tarjetanumero = document.getElementById("tarjetanumero");
	const tarjetapropietario = document.getElementById("tarjetapropietario");
	const tarjetacvv = document.getElementById("tarjetacvv");
	const tarjetacaducidad = document.getElementById("tarjetacaducidad");
	if (metodoPagoValue === "paypal") {
		paypalemail.parentNode.classList.remove("d-none");
		paypalpass.parentNode.classList.remove("d-none");
		tarjetanumero.parentNode.classList.add("d-none");
		tarjetapropietario.parentNode.classList.add("d-none");
		tarjetacvv.parentNode.classList.add("d-none");
		tarjetacaducidad.parentNode.classList.add("d-none");
	} else if (metodoPagoValue === "tarjeta") {
		paypalemail.parentNode.classList.add("d-none");
		paypalpass.parentNode.classList.add("d-none");
		tarjetanumero.parentNode.classList.remove("d-none");
		tarjetapropietario.parentNode.classList.remove("d-none");
		tarjetacvv.parentNode.classList.remove("d-none");
		tarjetacaducidad.parentNode.classList.remove("d-none");
	}
});