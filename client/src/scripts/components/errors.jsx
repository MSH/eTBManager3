import React from 'react';
import { isString } from '../commons/utils';

/**
 * Small module to handle error messages from the server
 */

export default function Errors(props) {

	const errs = props.messages;

	if (!errs) {
		return null;
	}

	// is a simple string representation ?
	if (isString(errs)) {
		return <div>{errs}</div>;
	}

	// no message found ?
	if (errs.length === 0) {
		return null;
	}

	const msgs = Errors.format(errs);

	// return component to be displayed
	return msgs.length === 1 ? <div>{msgs[0]}</div> :
		(<ul>
		{
			msgs.map((k, index) => <li key={index}>{k}</li>)
		}
		</ul>);
}

/**
 * Format the messages for displaying
 * @param  {[type]} messages [description]
 * @return {[type]}          [description]
 */
Errors.format = function(messages) {
	return messages.map(msg => msg.field ?
		msg.field + ': ' + msg.msg :
		msg.msg);
};

/**
 * Remove the messages that are from a specific field
 * @param  {[type]} field [description]
 * @return {[type]}       [description]
 */
Errors.remove = function(messages, field) {
	const res = [];
	return messages.filter(msg => {
		const isField = msg.field === field;

		if (isField) {
			res.push(msg);
		}

		return isField;
	});
};


Errors.propTypes = {
	messages: React.PropTypes.any
};
