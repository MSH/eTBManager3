<#import "lib/utils.ftl" as u>

<p>
    ${msg('mail.forgotpwd.1')}.
</p>

<@u.button
	label=msg('action.resetpwd')
	action="${url}#/pub/resetpwd/${user.passwordResetToken}" />

<p>
    ${msg('mail.forgotpwd.2')}.
</p>

<p>
    ${msg('mail.forgotpwd.3')}.
</p>
