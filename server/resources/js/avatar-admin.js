$j(document).ready(function () {
  $j('#suppliers-container').sortable();
});

function saveSuppliers(context_path) {
  var supplierList = [];
  var sortedSuppliers = $j('#suppliers-container').sortable('toArray');

  for (var i = 0; i < sortedSuppliers.length; i++) {
    var supplier = {
      id: sortedSuppliers[i],
      enabled: $j('#' + sortedSuppliers[i] + '_checkbox').prop('checked')
    };
    supplierList.push(supplier);
  }

  var suppliersJson = {suppliers: {supplier: supplierList}};
  var suppliersXml = x2js.json2xml_str(suppliersJson);

  BS.ajaxRequest(context_path + '/admin/avatarAdminConfiguration.html', {
    method: "POST",
    parameters: {suppliersXml: suppliersXml},
    onSuccess: function (transport) {
      $j('#errors').empty().hide();
      var errors = BS.XMLResponse.processErrors(transport.responseXML, {}, function (id, elem) {
        $j('#errors').append("<li>" + elem.firstChild.nodeValue + "</li>");
      });

      if (errors) {
        $j("#errors").slideDown("fast");
      }
      else {
        $j('#success').html(transport.responseText);
        $j('#success').slideDown("fast");
      }
    }
  });
}