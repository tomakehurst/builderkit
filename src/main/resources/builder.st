package $package$;

import java.util.List;
import java.util.Map;

public class $className$ {
	
	$if(isArray)
	$private JSONArray rootArray;
	$else
	$private JSONObject rootObject;
	$endif$
	
	$properties:{ property | 
private $property.value.type.javaClassNoPackage$ $property.value.name$;
	}$

}