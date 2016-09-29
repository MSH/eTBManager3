
import React from 'react';
import FormUtils from '../../../forms/form-utils';
import { ListBox } from '../../../components/index';
import { isPromise } from '../../../commons/utils';


/**
 * Control for yes-no selection
 */
export default class MultiListBoxControl extends React.Component {

    static typeName() {
        return 'multiListBox';
    }

    constructor(props) {
        super(props);
        this.onChange = this.onChange.bind(this);
    }

    componentWillMount() {
        if (!this.props.resources) {
            const options = FormUtils.createOptions(this.props.schema.options);
            if (options && !isPromise(options)) {
                this.setState({ options: options });
            }
        }
    }

    serverRequest(nextSchema, nextValue, nextResources) {
        return FormUtils.optionsRequest(this.props, nextSchema, nextValue, nextResources);
    }

    /**
     * Called when user selects an item in the drop down box
     * @return {[type]} [description]
     */
    onChange() {
        const vals = this.refs.selbox
            .getValue()
            .map(item => item.id);

        this.props.onChange({ schema: this.props.schema, value: vals });
    }

    render() {
        // get the schema passed by the parent
        const sc = this.props.schema || {};

        const options = this.props.resources || this.state.options;
        if (!options) {
            return null;
        }

        const wrapperClazz = sc.controlSize ? 'size-' + sc.controlSize : null;

        // rend the selection box
        return (
            <ListBox ref="selbox"
                options={options}
                optionDisplay="name"
                label={FormUtils.labelRender(sc.label, sc.required)}
                onChange={this.onChange}
                value={this.props.value}
                help={this.props.errors}
                wrapperClassName={wrapperClazz}
                bsStyle={this.props.errors ? 'error' : null}
                vertical={sc.vertical}
                textAlign={sc.textAlign}
                mode="multiple"
                maxHeight={sc.maxHeight} />
        );
    }
}

MultiListBoxControl.propTypes = {
    value: React.PropTypes.array,
    onChange: React.PropTypes.func,
    errors: React.PropTypes.any,
    schema: React.PropTypes.object,
    resources: React.PropTypes.array,
    noForm: React.PropTypes.bool,
    vertical: React.PropTypes.bool,
    textAlign: React.PropTypes.oneOf(['right', 'left', 'center'])
};
