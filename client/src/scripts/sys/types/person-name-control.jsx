import React from 'react';
import { Row, Col, FormControl, ControlLabel } from 'react-bootstrap';

import './person-name-control.less';

export default class PersonNameControl extends React.Component {

	static typeName() {
		return 'personName';
	}

	constructor(props) {
		super(props);
		this.onChange = this.onChange.bind(this);
	}

	onChange(evt) {
        console.log(evt.target.id, evt.target.value);
		if (this.props.onChange) {
			// const val = this.refs.input.getValue();
			// this.props.onChange({ schema: this.props.schema, value: val });
		}
	}

    render() {
        return (
            <div className="form-group person-name">
            <Row>
                <Col sm={12}>
                    <ControlLabel>{'Patient name:'}</ControlLabel>
                </Col>
            </Row>
            <Row>
                <Col sm={4}>
                    <FormControl id="first" type="text" placeholder="First name" onChange={this.onChange} />
                </Col>
                <Col sm={4}>
                    <FormControl id="middle" type="text" placeholder="Middle name" onChange={this.onChange} />
                </Col>
                <Col sm={4}>
                    <FormControl id="last" type="text" placeholder="Last name" onChange={this.onChange} />
                </Col>
            </Row>
            </div>
        );
    }
}


PersonNameControl.propTypes = {
	value: React.PropTypes.bool,
	onChange: React.PropTypes.func,
	errors: React.PropTypes.any,
	schema: React.PropTypes.object
};
