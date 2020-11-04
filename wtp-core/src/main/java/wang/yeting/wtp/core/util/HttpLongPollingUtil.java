package wang.yeting.wtp.core.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.exceptions.WtpConfigException;
import wang.yeting.wtp.core.exceptions.WtpConfigStatusCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;

/**
 * @author : weipeng
 * @date : 2020-07-23 20:07
 */

@Slf4j
public class HttpLongPollingUtil {

    private final Gson gson = new Gson();

    /**
     * Do get operation for the http request.
     *
     * @param httpRequest  the request
     * @param responseType the response type
     * @return the response
     * @throws WtpConfigException if any error happened or response code is neither 200 nor 304
     */
    public <T> HttpResponse<T> doGet(HttpRequest httpRequest, final Class<T> responseType) {
        Function<String, T> convertResponse = input -> gson.fromJson(input, responseType);
        return doGetWithSerializeFunction(httpRequest, convertResponse);
    }

    /**
     * Do get operation for the http request.
     *
     * @param httpRequest  the request
     * @param responseType the response type
     * @return the response
     * @throws WtpConfigException if any error happened or response code is neither 200 nor 304
     */
    public <T> HttpResponse<T> doGet(HttpRequest httpRequest, final Type responseType) {
        Function<String, T> convertResponse = input -> gson.fromJson(input, responseType);
        return doGetWithSerializeFunction(httpRequest, convertResponse);
    }

    private <T> HttpResponse<T> doGetWithSerializeFunction(HttpRequest httpRequest, Function<String, T> serializeFunction) {
        InputStreamReader isr = null;
        InputStreamReader esr = null;
        int statusCode;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(httpRequest.getUrl()).openConnection();

            conn.setRequestMethod("GET");

            Map<String, String> headers = httpRequest.getHeaders();
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            int connectTimeout = httpRequest.getConnectTimeout();
            if (connectTimeout <= 0) {
                connectTimeout = 1000;
            }

            int readTimeout = httpRequest.getReadTimeout();
            if (readTimeout <= 0) {
                readTimeout = 5000;
            }

            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            conn.connect();

            statusCode = conn.getResponseCode();
            String response;

            try {
                isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                response = charStreamsToString(isr);
            } catch (IOException ex) {
                /**
                 * according to https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html,
                 * we should clean up the connection by reading the response body so that the connection
                 * could be reused.
                 */
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    esr = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
                    try {
                        charStreamsToString(esr);
                    } catch (IOException ioe) {
                        //ignore
                    }
                }
                // 200 and 304 should not trigger IOException, thus we must throw the original exception out
                if (statusCode == 200 || statusCode == 304) {
                    throw ex;
                }
                // for status codes like 404, IOException is expected when calling conn.getInputStream()
                throw new WtpConfigStatusCodeException(statusCode, ex);
            }
            if (statusCode == 200) {
                return (HttpResponse<T>) serializeFunction.apply(response);
            }
            if (statusCode == 304) {
                return new HttpResponse<>(statusCode, null);
            }
        } catch (WtpConfigStatusCodeException e) {
            throw e;
        } catch (Throwable ex) {
            throw new WtpConfigException("Could not complete get operation", ex);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
            if (esr != null) {
                try {
                    esr.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
        throw new WtpConfigStatusCodeException(statusCode, String.format("Get operation failed for %s", httpRequest.getUrl()));
    }

    public static String charStreamsToString(Readable r) throws IOException {
        return toStringBuilder(r).toString();
    }

    private static StringBuilder toStringBuilder(Readable r) throws IOException {
        StringBuilder sb = new StringBuilder();
        copy(r, sb);
        return sb;
    }

    public static long copy(Readable from, Appendable to) throws IOException {
        checkNotNull(from);
        checkNotNull(to);
        CharBuffer buf = CharBuffer.allocate(2048);
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += (long) buf.remaining();
            buf.clear();
        }
        return total;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

}
