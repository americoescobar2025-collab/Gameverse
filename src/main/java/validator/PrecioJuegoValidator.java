package validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("precioJuegoValidator")
public class PrecioJuegoValidator implements Validator<Object> {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Precio inválido.",
                    "Debe ingresar un valor numérico."
            ));
        }

        if (precio <= 0.0) {
            FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Precio no permitido.",
                    "El precio del videojuego debe ser mayor a $0.00."
            );
            throw new ValidatorException(msg);
        }
    }
}
