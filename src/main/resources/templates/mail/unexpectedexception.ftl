<#-- Unexpected Error
 Contains information about an unexpected error that occourred and is sent to admin users
-->
<#import "lib/utils.ftl" as u>

<p>
    <b>Date-time:</b> ${errorDate}
</p>

<p>
    <b>URL:</b> ${excepionUrl}
</p>

<p>
    <b>User:</b> ${userName}
</p>

<p>
    <b>Workspace:</b> ${workspace}
</p>

<p>
    <b>Exception:</b> ${exceptionClass}
</p>

<p>
    <b>Exception message:</b> ${exceptionMessage}
</p>

<p>
${stackTrace}
</p>