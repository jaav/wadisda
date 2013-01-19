package be.virtualsushi.wadisda.pages;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import be.virtualsushi.wadisda.entities.Relation;
import be.virtualsushi.wadisda.services.repository.ListJpaRepository;

/**
 * Start page of application wadisda.
 */
public class Index {
	@Property
	@Inject
	@Symbol(SymbolConstants.TAPESTRY_VERSION)
	private String tapestryVersion;

	@InjectComponent
	private Zone zone;

	@Persist
	@Property
	private int clickCount;

	@Inject
	private AlertManager alertManager;

	@Inject
	private ListJpaRepository listJpaRepository;

	@Property
	private List<Relation> relations;

	@Property
	private Relation relation;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	public Date getCurrentTime() {
		return new Date();
	}

	void onActionFromIncrement() {
		alertManager.info("Increment clicked");

		clickCount++;
	}

	Object onActionFromIncrementAjax() {
		clickCount++;

		alertManager.info("Increment (via Ajax) clicked");

		return zone;
	}

	@PageLoaded
	void pageLoaded() {
		relations = listJpaRepository.getValuesList(Relation.class);
	}

	@AfterRender
	public void afterRender() {
		javaScriptSupport.addScript("$('#%s').bind(Tapestry.ZONE_UPDATED_EVENT, function(){$('#dialog').modal();});", zone.getClientId());
	}

}
