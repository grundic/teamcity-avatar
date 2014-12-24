<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="ext" tagdir="/WEB-INF/tags/ext" %>
<%@ taglib prefix="userProfile" tagdir="/WEB-INF/tags/userProfile" %>

<jsp:useBean id="selectedAvatarSupplier" scope="request" type="java.lang.String"/>
<jsp:useBean id="suppliers" scope="request"
             type="java.util.Map<java.lang.String, ru.mail.teamcity.avatar.supplier.AvatarSupplier>"/>
<jsp:useBean id="currentUser" scope="request" type="jetbrains.buildServer.users.SUser"/>

<l:settingsBlock title="Avatar configuration">
  <div class="clearfix">

    <form id="avatarConfigurationForm" action="">
      <label for="avatarSupplierType">Choose avatar supplier:</label>

      <div>
        <select id="avatarSupplierType" name="avatarSupplierType">
          <option value="auto">-- Automatic --</option>
          <c:forEach var="supplier" items="${suppliers}">
            <option value="${supplier.key}"
                    <c:if test="${selectedAvatarSupplier == supplier.key}">selected</c:if>
                    >
                ${supplier.value.optionName}
            </option>
          </c:forEach>
        </select>
      </div>

      <div id="supplierTemplate"></div>

      <input id="avatarConfigurationFormSubmit" type="submit" value="Save" class="btn"/>
    </form>

  </div>

</l:settingsBlock>

<ul id="errors" class="hidden avatar-validation"></ul>
<div id="success" class="hidden avatar-success"></div>