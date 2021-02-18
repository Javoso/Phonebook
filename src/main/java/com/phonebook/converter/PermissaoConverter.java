package com.phonebook.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.phonebook.model.Permissao;


@FacesConverter(forClass = Permissao.class, value = "permissaoConverter")
public class PermissaoConverter implements Converter {

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value == null || value.isEmpty())
			return null;

		return this.getAttributesFrom(component).get(value);
	}
	  
	@Override
	public String getAsString(FacesContext ctx, UIComponent componente, Object value) {

		if (value != null) {
			Permissao entity = (Permissao) value;

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
	  
	protected void addAttribute(UIComponent component, Permissao permissao) {
		String key = permissao.getId().toString(); 
		this.getAttributesFrom(component).put(key, permissao);
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}
	
}