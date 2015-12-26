
import { format } from './utils';

export default {
	NotValid: __('NotValid'),
	NotNull: __('NotNull'),
	NotValidPassword: __('NotValidPassword'),
	NotValidEmail: __('NotValidEmail'),
	minValue: (val) => format(__('validation.client.min'), val),
	maxValue: (val) => format(__('validation.client.max'), val)
};
