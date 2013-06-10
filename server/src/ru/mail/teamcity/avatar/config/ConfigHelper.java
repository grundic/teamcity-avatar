package ru.mail.teamcity.avatar.config;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mail.teamcity.avatar.supplier.AvatarSupplier;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Grigory Chernyshev
 * Date: 18.05.13 11:02
 */
public class ConfigHelper {

  private final static Logger LOG = Logger.getInstance(ConfigHelper.class.getName());

  private final static String CONFIG_NAME = "avatar-config.xml";

  /**
   * Read saved configuration from file
   *
   * @return Suppliers bean
   * @throws JAXBException
   * @throws FileNotFoundException
   */
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

  /**
   * Read saved configuration from stream
   *
   * @return Suppliers bean
   * @throws JAXBException
   */
  @NotNull
  public static Suppliers read(@NotNull InputStream stream) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(Suppliers.class);
    Unmarshaller um = context.createUnmarshaller();
    return (Suppliers) um.unmarshal(stream);
  }

  /**
   * Read saved configuration from file and supplement with missing suppliers.
   *
   * @return Suppliers bean
   */
  public static Suppliers load(@NotNull String serverConfigDir, @NotNull Map<String, AvatarSupplier> suppliers) {
    // Load previously saved suppliers from config
    Suppliers suppliersBean;
    try {
      suppliersBean = ConfigHelper.read(serverConfigDir);
    } catch (Exception e) {
      LOG.error(e.getMessage());
      suppliersBean = new Suppliers();
    }

    assert suppliersBean != null;
    List<String> suppliersID = new ArrayList<String>();
    List<Supplier> itemsToRemove = new ArrayList<Supplier>();

    // Get supplier instance by id. If it's not found - remove it
    for (Supplier supplierBean : suppliersBean.getSupplierList()) {
      AvatarSupplier supplier = suppliers.get(supplierBean.getId());
      if (null == supplier) {
        LOG.error(String.format("Failed to get supplier by %s id!", supplierBean.getId()));
        itemsToRemove.add(supplierBean);
        continue;
      }
      suppliersID.add(supplierBean.getId());
    }
    suppliersBean.getSupplierList().removeAll(itemsToRemove);


    // iterate over suppliers loaded from beans and check, that supplier was saved in config
    for (String id : suppliers.keySet()) {
      if (!suppliersID.contains(id)) {
        suppliersBean.addSupplier(new Supplier(id, true));
      }
    }

    return suppliersBean;
  }

  /**
   * Write suppliers bean to configuration file
   *
   * @throws JAXBException
   */
  public static void write(@NotNull Suppliers suppliers, @NotNull String serverConfigDir) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(suppliers.getClass());
    Marshaller m = context.createMarshaller();
    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    m.marshal(suppliers, new File(serverConfigDir + "/" + CONFIG_NAME));
  }
}