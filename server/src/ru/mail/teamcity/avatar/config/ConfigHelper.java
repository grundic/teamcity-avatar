package ru.mail.teamcity.avatar.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * User: Grigory Chernyshev
 * Date: 18.05.13 11:02
 */
public class ConfigHelper {

  public final static String CONFIG_NAME = "avatar-config.xml";

  @Nullable
  public static Suppliers read(@NotNull String serverConfigDir) throws JAXBException, FileNotFoundException {
    JAXBContext context = JAXBContext.newInstance(Suppliers.class);
    Unmarshaller um = context.createUnmarshaller();
    File file = new File(serverConfigDir, CONFIG_NAME);
    if (!file.isFile()) {
      return new Suppliers();
    }
    return (Suppliers) um.unmarshal(file);
  }

  @NotNull
  public static Suppliers read(@NotNull InputStream stream) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(Suppliers.class);
    Unmarshaller um = context.createUnmarshaller();
    return (Suppliers) um.unmarshal(stream);
  }

  public static void write(@NotNull Suppliers suppliers, @NotNull String serverConfigDir) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(suppliers.getClass());
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.marshal(suppliers, new File(serverConfigDir + "/" + CONFIG_NAME));
  }
}