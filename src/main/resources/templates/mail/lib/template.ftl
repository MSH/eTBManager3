<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>e-TB Manager message</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body style="margin: 0; padding: 20px; background-color: #e0e0e0; font-family: Verdana, Arial, sans-serif; font-size:14px; line-height:20px">
<table border="1" cellpadding="20px" cellspacing="0" width="100%"
       style="border-collapse: collapse;border:1px solid #e0e0e0;">
    <tr>
        <td bgcolor="#303e3c" style="color:#ffff00;font-size:22px;border:1px solid #303e3c">
		${subject}
            <div style="font-size: 14px;color:#86928B">e-TB Manager</div>
        </td>
    </tr>
    <tr>
        <td bgcolor="#fff" style="border:1px solid #fff">
            <p>
			${msg("mail.hi", name)},
            </p>

            <p>
			<#include content/>
            </p>
		${msg('mail.regards')},
            <br/>
		${msg('mail.signature')}
            <p style="color:#909090;font-size:12px">
			${msg('mail.introduction')}
            </p>
        </td>
    </tr>
</table>
</body>
</html>