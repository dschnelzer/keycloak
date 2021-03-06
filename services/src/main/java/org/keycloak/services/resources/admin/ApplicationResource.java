package org.keycloak.services.resources.admin;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.logging.Logger;
import org.keycloak.models.*;
import org.keycloak.representations.idm.*;
import org.keycloak.services.managers.ApplicationManager;
import org.keycloak.services.managers.RealmManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ApplicationResource extends RoleContainerResource {
    protected static final Logger logger = Logger.getLogger(RealmAdminResource.class);
    protected RealmModel realm;
    protected ApplicationModel application;
    protected KeycloakSession session;

    public ApplicationResource(RealmModel realm, ApplicationModel applicationModel, KeycloakSession session) {
        super(applicationModel);
        this.realm = realm;
        this.application = applicationModel;
        this.session = session;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(final ApplicationRepresentation rep) {
        ApplicationManager applicationManager = new ApplicationManager(new RealmManager(session));
        applicationManager.updateApplication(rep, application);
    }


    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public ApplicationRepresentation getApplication() {
        ApplicationManager applicationManager = new ApplicationManager(new RealmManager(session));
        return applicationManager.toRepresentation(application);
    }

    @Path("credentials")
    @PUT
    @Consumes("application/json")
    public void updateCredentials(List<CredentialRepresentation> credentials) {
        logger.info("updateCredentials");
        if (credentials == null) return;

        for (CredentialRepresentation rep : credentials) {
            UserCredentialModel cred = RealmManager.fromRepresentation(rep);
            realm.updateCredential(application.getApplicationUser(), cred);
        }
    }

    @Path("scope-mappings")
    public ScopeMappedResource getScopeMappedResource() {
        return new ScopeMappedResource(realm, application.getApplicationUser(), session);
    }


}
