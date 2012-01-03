package $package$;

import java.util.List;
import java.util.Map;

public class $className$ {
	
	$if(isArray)
	$private JSONArray rootArray;
	
public $className$() {
	rootArray = new JSONArray();
}
	$else
	$private Map<String, Object> rootObject;

@SuppressWarnings("unchecked")	
public $className$() {
	rootObject = new JSONObject();
}
	$endif$
	
	$properties:{ property | 
public $className$ with$property.nameFirstLetterUppercase$($property.type.javaClassNoPackage$ $property.name$) {
	rootObject.put("$property.name$", $property.name$);
	return this;
\}

	}$
	
	public String asJson() {
		return rootObject.toString();
	}

}