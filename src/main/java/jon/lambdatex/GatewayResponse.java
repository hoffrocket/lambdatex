package jon.lambdatex;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;


/**
 * The response that will be the passed back to the API Gateway.
 * <p>
 * The implementation depends on the AWS API Gateway response template and
 * is designed to get serialized to it.
 *
 * @author Bjoern Bilger
 *
 */
public final class GatewayResponse {

  private final String body;
  private final Map<String, String> headers;
  private final int statusCode;
  private final boolean base64Encoded;

  public GatewayResponse(String body, Map<String, String> headers,
                         int statusCode, boolean base64Encoded) {
    requireNonNull(headers);
    this.statusCode = statusCode;
    this.body = body;
    this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
    this.base64Encoded = base64Encoded;
  }

  public String getBody() {
    return body;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public int getStatusCode() {
    return statusCode;
  }
  // APIGW expects the property to be called "isBase64Encoded"
  public boolean isIsBase64Encoded() {
    return base64Encoded;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (!getClass().equals(other.getClass())) {
      return false;
    }
    GatewayResponse castOther = (GatewayResponse) other;
    return Objects.equals(body, castOther.body)
        && Objects.equals(headers, castOther.headers)
        && Objects.equals(statusCode, castOther.statusCode)
        && Objects.equals(base64Encoded, castOther.base64Encoded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body, headers, statusCode, base64Encoded);
  }

  @Override
  public String toString() {
    return "GatewayResponse [body=" + body + ", headers=" + headers + ", statusCode=" + statusCode
        + ", base64Encoded=" + base64Encoded + "]";
  }
}
