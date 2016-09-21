import { format } from './utils';

export default {
    NotValid: __('NotValid'),
    NotNull: __('NotNull'),
    NotValidPassword: __('NotValidPassword'),
    NotValidEmail: __('NotValidEmail'),
    minValue: (val) => format(__('validation.client.min'), val),
    maxValue: (val) => format(__('validation.client.max'), val),
    minListQtt: (val) => format(__('validation.client.list.min'), val),
    maxListQtt: (val) => format(__('validation.client.list.max'), val)
};
