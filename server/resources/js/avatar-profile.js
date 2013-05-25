$j(document).ready(function () {
  loadSupplierTemplate($j('#avatarSupplierType').val());

  $j('#avatarSupplierType').change(function () {
    $j("#errors").hide();
    $j('#success').hide();
    loadSupplierTemplate($j(this).val());
  });

  $j("#avatarConfigurationFormSubmit").click(function (event) {
    event.preventDefault();
    saveSupplier();
  });
});

function loadSupplierTemplate(supplier) {
  var url = window['base_uri'] + "/avatarSupplierAjax.html?avatarSupplierType=" + supplier;
  BS.ajaxRequest(encodeURI(url), {
            onSuccess: function (transport) {
              $j('#supplierTemplate').html(transport.responseText);
            }
          }
  );
}

function saveSupplier() {
  BS.ajaxRequest(window['base_uri'] + '/avatarProfile.html', {
    method: "POST",
    parameters: $j("#avatarConfigurationForm").serialize(),
    onSuccess: function (transport) {
      $j('#errors').empty();
      var errors = BS.XMLResponse.processErrors(transport.responseXML, {}, function (id, elem) {
        $j('#errors').append("<li>" + elem.firstChild.nodeValue + "</li>");
      });

      if (errors) {
        $j("#errors").hide().slideDown("fast");
      }
      else {
        $j('#success').html(transport.responseText);
        $j('#success').hide().slideDown("fast");

        delete Avatar._cache[Avatar.currentUser.extendedName];
        $j("#avatar-profile").remove();

        Avatar.addAvatarToUserPanel();
      }
    }
  });
}