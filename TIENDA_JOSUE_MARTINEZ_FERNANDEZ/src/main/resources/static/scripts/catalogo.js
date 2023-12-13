botonesResta = document.getElementsByClassName("resta");
    botonesSuma = document.getElementsByClassName("suma");

    for (const botonResta of botonesResta) {
      botonResta.addEventListener("click", () => {
    	  if (botonResta.parentNode.getElementsByClassName("cantidadProducto")[0].value >= 1) {
    	        botonResta.parentNode.getElementsByClassName("cantidadProducto")[0].value--; 
    	  }
      });
    }

    for (const botonSuma of botonesSuma) {
      botonSuma.addEventListener("click", () => {
    	  if (botonSuma.parentNode.getElementsByClassName("cantidadProducto")[0].value < parseInt(botonSuma.parentNode.getElementsByClassName("cantidadMaximaProducto")[0].value)) {
    	        botonSuma.parentNode.getElementsByClassName("cantidadProducto")[0].value++;
    	  }
      });
    }