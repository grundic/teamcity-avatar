<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="ext" tagdir="/WEB-INF/tags/ext" %>
<%@ taglib prefix="userProfile" tagdir="/WEB-INF/tags/userProfile" %>

<jsp:useBean id="selectedAvatarSupplier" scope="request" type="java.lang.String"/>
<jsp:useBean id="suppliers" scope="request" type="java.util.List<ru.mail.teamcity.avatar.supplier.Supplier>"/>
<jsp:useBean id="currentUser" scope="request" type="jetbrains.buildServer.users.SUser"/>

<script type="text/javascript">

  $j(document).ready(function () {

    loadSupplierTemplate($j('#avatarSupplierType').val());

    $j('#avatarSupplierType').change(function () {
      loadSupplierTemplate($j(this).val());
    });
  });

  function loadSupplierTemplate(supplier) {
    url = window['base_uri'] + "/avatarSupplierAjax.html?avatarSupplierType=" + supplier;
    BS.ajaxRequest(encodeURI(url), {
              onSuccess: function (transport) {
                $j('#supplierTemplate').html(transport.responseText);
              }
            }
    );
  }
</script>

<l:settingsBlock title="Avatar configuration">


  <div class="clearfix">

    <form id="avatarConfigurationForm" action="profileAvatarConfig.html" method="POST">
      <label for="avatarSupplierType">Choose avatar supplier:</label>

      <div>
        <select id="avatarSupplierType" name="avatarSupplierType">
          <c:forEach var="supplier" items="${suppliers}">
            <option value="${supplier.toString()}"
                    <c:if test="${selectedAvatarSupplier == supplier.toString()}">selected</c:if>>
                ${supplier.get().optionName}
            </option>
          </c:forEach>
        </select>
      </div>


      <div id="supplierTemplate"
           style="margin-top: 20px; margin-bottom: 15px; margin-left: 10px; border: 2px; border-style:dotted; padding: 10px">
      </div>

      <input type="submit" value="Save" class="btn"/>
    </form>

  </div>

</l:settingsBlock>