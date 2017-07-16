import play.api.mvc.EssentialFilter;
import play.filters.headers.SecurityHeadersFilter;
import play.http.HttpFilters;

import javax.inject.Inject;

public class Filters1 implements HttpFilters {

    @Inject
    SecurityHeadersFilter securityHeadersFilter;

    public EssentialFilter[] filters() {
        return new EssentialFilter[] { securityHeadersFilter };
    }
}
