import React from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';
import { server } from '../commons/server';
import { isFunction, isString, objEqual } from '../commons/utils';
import Form from './form';

const requiredTooltip = (
    <Tooltip id="required">{__('NotNull')}</Tooltip>
    );

export default class FormUtils {

    /**
     * Generate a new label component to be displayed as the label of an input control.
     * Includes a red icon on the right side to indicate required fields
     * @param  {[type]} schema [description]
     * @return {[type]}        [description]
     */
    static labelRender(label, required) {
        if (!label) {
            return null;
        }

        const txt = label + ':';

        return required ?
            <OverlayTrigger placement="top" overlay={requiredTooltip}>
                <span>{txt}<i className="fa fa-exclamation-circle app-required"/></span>
            </OverlayTrigger> :
            txt;
    }

    static readOnlyRender(content, label) {
        const labelelem = label ? <label className="control-label">{FormUtils.labelRender(label)}</label> : null;
        return (
            <div className="form-group">
                {labelelem}
                <div className="form-control-static autoscroll">
                    {content ? content : '-'}
                </div>
            </div>
            );
    }


    /**
     * Return the server request of the options, if available
     * @param  {[type]} schema The element schema
     * @param  {[type]} doc    The document of the form
     * @return {[type]}        [description]
     */
    static optionsRequest(props, nextSchema, nextValue, nextResources) {
        const options = nextSchema.options;

        const params = props.schema ? props.schema.params : null;

        if ((props.resources || nextResources) &&
            props.schema.options === options &&
            objEqual(params, nextSchema.params)) {
            return null;
        }

        return isString(options) ? { cmd: options, params: nextSchema.params } : null;
    }

    /**
     * Create the options for a select box control
     * @param  {[type]} options [description]
     * @return {[type]}         [description]
     */
    static createOptions(options, resources) {
        if (!options && !resources) {
            return null;
        }

        // if options is a string, so it was (probably) resolved before,
        // so return the resources instead
        const lst = typeof options === 'string' ? resources : options;

        if (!lst) {
            return null;
        }

        // options is an array ?
        if (Array.isArray(lst)) {
            return lst;
        }

        let opts = [];
        if (lst.constructor !== Array && typeof lst === 'object') {
            const keys = Object.keys(lst);
            if (keys.length === 2 && lst.from && lst.to) {
                for (var i = lst.from; i <= lst.to; i++) {
                    opts.push({ id: i.toString(), name: i.toString() });
                }
            }
            else {
                keys.forEach(key => opts.push({ id: key, name: lst[key] }));
            }
        }
        else {
            opts = opts.concat(lst);
        }

        // create component for list of options
        return opts;
    }


    /**
     * Request the server to initialize the given fields
     * @param  {Array} req list of field objects with id, type and value
     * @return {Promise}   Promise to be resolved when server answers back
     */
    static serverRequest(req) {
        // multiple requests ?
        const mult = Array.isArray(req);
        // create the data request to be posted
        const data = mult ?
            req :
            [{
                cmd: req.cmd,
                id: 'v',
                params: req.params
            }];

        return server.post('/api/form/request', data)
            .then(res => mult ? res : res.v);
    }

    /**
     * Return the component referenced by the type property in the schema.
     * The component may be a string reference to a form type or a reference
     * to a React Component
     * @param  {[type]} schema [description]
     * @return {[type]}        [description]
     */
    static getControl(schema) {
        if (__DEV__) {
            if (!schema.type) {
                throw new Error('No control type defined for property ' + schema.property);
            }
        }
        return typeof schema.type === 'string' ? Form.types[schema.type] : schema.type;
    }

    /**
     * Evaluate a property. If property is a function, the function is evaluated in the context
     * of the give document
     * @param  {Object} sc       Schema to evaluate the properthy
     * @param  {string} property The property name
     * @param  {Object} doc      The document to be used as context of the property
     * @return {any}             The property value
     */
    static propEval(sc, property, doc) {
        const val = sc[property];
        // property value is a function ?
        if (!isFunction(val)) {
            return;
        }

        // evaluate the function in the context of the document and usign the doc as argument
        const res = val.call(doc, doc);
        sc[property] = res;
    }
}
