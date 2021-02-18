package com.phonebook.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.phonebook.model.Usuario;


@FacesConverter(forClass = Usuario.class, value = "usuarioConverter")
public class UsuarioConverter implements Converter {

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value == null || value.isEmpty())
			return null;

		return this.getAttributesFrom(component).get(value);
	}
	  
	@Override
	public String getAsString(FacesContext ctx, UIComponent componente, Object value) {

		if (value != null) {
			Usuario entity = (Usuario) value;

			if (entity.getId() != null) {
				this.addAttribute(componente, entity);

				if (entity.getId() != null) {
					return String.valueOf(entity.getId());
				}

				return (String) value;
			}
		}

		return "";
	}
	  
	protected void addAttribute(UIComponent component, Usuario usuario) {
		String key = usuario.getId().toString(); 
		this.getAttributesFrom(component).put(key, usuario);
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}
	
}