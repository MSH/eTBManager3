
import React from 'react';
import { Card, Title } from '../../components/index';

export function initau() {
	return new Promise(resolve => {
		resolve(AdmUnits);
	});
}

/**
 * The page controller of the public module
 */
class AdmUnits extends React.Component {

	render() {
		return (
			<Card>
				<h1>{'Administrative units'}</h1>
			</Card>
			);
	}
}
