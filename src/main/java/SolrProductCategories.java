import java.util.List;

import javax.xml.transform.Source;

import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class SolrProductCategories {

	private static RestTemplate restTemplate = new RestTemplate();
	private static XPathOperations template = new Jaxp13XPathTemplate();

	private static String[] catalogs = { "US_CATALOG", "AR_CATALOG", "AT_CATALOG", "AU_CATALOG", "BE_CATALOG", "BR_CATALOG", "CA_CATALOG", "CL_CATALOG", "FI_CATALOG", "DE_CATALOG", "DK_CATALOG", "ES_CATALOG", "FR_CATALOG", "GB_CATALOG", "IT_CATALOG", "MX_CATALOG", "NL_CATALOG", "NO_CATALOG",
			"PL_CATALOG",
			"SE_CATALOG", "ZA_CATALOG", "DIGITAL", "GARMIN", "SI_CATALOG", "SSA", "OEM_Store", "CHE", "NZ", "EASTERN_EUROPE", "MENA", "HR_CATALOG" };
	private static String[] categories = { "OnTheRoad", "InTheAir", "IntoSports", "OnTheWater", "OnTheGo", "OEM" };

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		for (String category : categories) {
			for (String catalog : catalogs) {
				Source products = restTemplate.getForObject("http://olaxpw-buysrch10.garmin.com/searchserver/product/select?qt=standard&q=(productCategory_" + catalog + ":" + category + ")", Source.class);

				List<String> productUids = template.evaluate("//doc", products, new NodeMapper() {
					@Override
					public Object mapNode(Node node, int i) throws DOMException {
						Element doc = (Element) node;
						Element docLong = (Element) doc.getLastChild();
						Text docLongText = (Text) docLong.getFirstChild();
						return docLongText.getData();
					}
				});

				for (String productUid : productUids) {
					System.out.println(productUid);
				}
			}
		}
	}
}
