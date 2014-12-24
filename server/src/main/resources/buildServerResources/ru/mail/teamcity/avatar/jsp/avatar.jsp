<jsp:useBean id="currentUser" type="jetbrains.buildServer.users.SUser" scope="request"/>

<script type="text/javascript">
  Avatar.currentUser = {
    'id': '${currentUser.id}',
    'username': '${currentUser.username}',
    'name': '${currentUser.name}',
    'descriptiveName': '${currentUser.descriptiveName}',
    'extendedName': '${currentUser.extendedName}',
    'email': '${currentUser.email}'
  };
</script>