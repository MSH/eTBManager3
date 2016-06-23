<#-- Password reset request content
 Contains instructions and link on how to reset the password in case the user forgot it
-->
<#import "lib/utils.ftl" as u>

<p>
   ${msg('mail.newuser.msg')}:
</p>

<@u.panel>
	<b>User: </b>${user.login}
</@u.panel>

<p>
${msg('mail.newuser.msg1')}.
</p>

<@u.button
	label=msg('userreg.mail.confirm')
	action="${url}#/pub/confirmemail/${user.passwordResetToken}" />

<p>
${msg('userreg.mail.2')}.
</p>
