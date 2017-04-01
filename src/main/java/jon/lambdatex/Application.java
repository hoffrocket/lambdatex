package jon.lambdatex;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.Base64;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;


public class Application implements RequestHandler<Map<String,Object>, GatewayResponse> {
  static final int DefaultSize = 24;

  public GatewayResponse usageError() {
    return new GatewayResponse(Base64.encodeAsString(createError(DefaultSize, "Pass formula into query string ?q=<urlencodedformula>&s=<font size>")),
        Collections.singletonMap("content-type", "image/png"), 400, true);
  }

  @Override
  public GatewayResponse handleRequest(Map<String,Object> input, Context context) {
    Map<String, String> params = (Map<String, String>)input.get("queryStringParameters");
    if (params == null) {
      return usageError();
    }
    int size = 0;
    try {
      size = Integer.parseInt(params.getOrDefault("s", String.valueOf(DefaultSize)));
    } catch (NumberFormatException e) {
      return usageError();
    }
    String formulaString = params.get("q");
    if (formulaString == null) {
      return usageError();
    }
    try {
      TeXFormula formula = new TeXFormula(formulaString);
      TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(size).setFGColor(Color.black).build();
      BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = image.createGraphics();
      g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
      icon.paintIcon(null, g2, 0, 0);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try {
        ImageIO.write(image, "png", os);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      return new GatewayResponse(Base64.encodeAsString(os.toByteArray()), Collections.singletonMap("content-type", "image/png"), 200, true);
    } catch (ParseException e){
      return new GatewayResponse(Base64.encodeAsString(createError(DefaultSize, e.getMessage())), Collections.singletonMap("content-type", "image/png"), 400, true);
    }
  }

  public byte[] createError(int fontSize, String message) {
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    Font font = new Font("Arial", Font.PLAIN, fontSize);
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();
    int width = fm.stringWidth(message);
    int height = fm.getHeight();
    g2d.dispose();

    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    g2d = img.createGraphics();
    g2d.setFont(font);
    fm = g2d.getFontMetrics();
    g2d.setColor(Color.BLACK);
    g2d.drawString(message, 0, fm.getAscent());
    g2d.dispose();
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      ImageIO.write(img, "png", os);
      return os.toByteArray();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
