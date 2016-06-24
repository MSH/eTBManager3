<#-- Standard button with a label and an action -->
<#macro button label action>
	<p style="padding:20px;text-align:center; margin:10px">
		<a href="${action}"
		   style="font-size:1.1em;padding: 8px; border: 2px solid #228bc3;color: #228bc3;border-radius: 4px;text-decoration: none">
			${label}
		</a>
	</p>
</#macro>


<#-- Displays a content inside a simple panel -->
<#macro panel>
	<p style="background:#f8f8f8;border-radius: 4px;border:1px solid #f0f0f0;padding:8px;">
		<#nested>
	</p>
</#macro>

