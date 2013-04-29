<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="ext" tagdir="/WEB-INF/tags/ext" %>
<%@ taglib prefix="userProfile" tagdir="/WEB-INF/tags/userProfile" %>

<l:settingsBlock title="Avatar configuration">

  <div class="clearfix">
    <form id="avatarConfigurationForm" action="profileAvatarConfig.html" method="POST"
          onsubmit="return BS.NotifierPropertiesForm.submitSettings()">


      <div class="avatarConfigurationControls">
        <label for="avatarUrl">Avatar url:</label>
        <input class="textfield" type="text" id="avatarUrl" name="avatarUrl" style="width: 30em;" maxlength="256"
               value=""/>
      </div>

      <input type="submit" value="Save" class="btn btn_mini submitButton"/>

      <%--<img id="saving_settings" style="display: none; " class="progressRing progressRingDefault"--%>
           <%--src="/bs/img/ajax-loader.gif" width="16" height="16" alt="Please wait..." title="Please wait..."/>--%>
    </form>
  </div>


</l:settingsBlock>