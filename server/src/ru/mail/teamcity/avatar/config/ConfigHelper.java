package ru.mail.teamcity.avatar.config;

import jetbrains.buildServer.log.Loggers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * User: Grigory Chernyshev
 * Date: 18.05.13 11:02
 */
public class ConfigHelper {
  @Nullable
  public static Object read(Class objectClass, String path) {
    try {
      JAXBContext context = JAXBContext.newInstance(objectClass);
      Unmarshaller um = context.createUnmarshaller();
      return um.unmarshal(new FileReader(path));
    } catch (JAXBException e) {
      Loggers.SERVER.error(String.format("Failed to read %s: %s", path, e.getMessage()));
    } catch (FileNotFoundException e) {
      Loggers.SERVER.error(String.format("Failed to read %s: %s", path, e.getMessage()));
    }
    return null;
  }

  public static void write(@NotNull Object dataObject, String path) {
    try {
      JAXBContext context = JAXBContext.newInstance(dataObject.getClass());
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      m.marshal(dataObject, new File(path));
    } catch (JAXBException e) {
      Loggers.SERVER.error(String.format("Failed to write %s: %s", path, e.getMessage()));
    }
  }
}
