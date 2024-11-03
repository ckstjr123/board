package hello.qnaboard.controller;

import org.springframework.util.*;

import java.util.Iterator;
import java.util.Optional;

public class QueryParamBuilder {

    private final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    protected QueryParamBuilder() {}

    public static QueryParamBuilder newInstance() {
        return new QueryParamBuilder();
    }


    public QueryParamBuilder queryParam(String name, Object... values) {
        Assert.notNull(name, "Name must not be null");
        if (!ObjectUtils.isEmpty(values)) {
            Object[] var3 = values;
            int var4 = values.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Object value = var3[var5];
                String valueAsString = this.getQueryParamValue(value);
                this.queryParams.add(name, valueAsString);
            }
        } else {
            this.queryParams.add(name, null);
        }

        return this;
    }

    private String getQueryParamValue(Object value) {
        if (value != null) {
            return value instanceof Optional ?
                    (String) ((Optional) value).map(Object::toString).orElse(null) : value.toString();
        } else {
            return null;
        }
    }

    public String toQueryParamString() {
        if (!this.queryParams.isEmpty()) {
            StringBuilder queryBuilder = new StringBuilder();
            this.queryParams.forEach((name, values) -> {
                if (CollectionUtils.isEmpty(values)) {
                    if (queryBuilder.length() != 0) {
                        queryBuilder.append('&');
                    }
                    queryBuilder.append(name);
                } else {
                    Iterator var3 = values.iterator();

                    while (var3.hasNext()) {
                        Object value = var3.next();

                        String queryStrVal = (value != null) ? value.toString() : "";
                        if (!queryStrVal.isEmpty()) {
                            if (queryBuilder.length() != 0) {
                                queryBuilder.append('&');
                            }

                            queryBuilder.append(name).append('=').append(queryStrVal);
                        }
                    }
                }
            });
            return queryBuilder.toString();
        } else {
            return null;
        }
    }

}
