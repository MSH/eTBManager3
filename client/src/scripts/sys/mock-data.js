/**
 * Generate mock data for displaying purposes
 * @return {[type]} [description]
 */


export function generateName() {
	const gender = Math.random() >= 0.5 ? 'MALE' : 'FEMALE';

	const name = (gender === 'MALE' ?
		firstNamesMale[randonIndex(firstNamesMale.length)] :
		firstNamesFemale[randonIndex(firstNamesFemale.length)]) + ' ' +
		lastNames[randonIndex(lastNames.length)];

	const age = Math.round((Math.random() * 40) + 10);

	return {
		name: name,
		gender: gender,
		age: age,
		id: Math.round(Math.random() * 10000000)
	};
}


export function generateCaseNumber() {
	return Math.round((Math.random() * 10000) + 10000) + '-' + Math.round(Math.random() * 10);
}


const tags = [
	{
		id: '123456-1',
		name: 'Not on treatment',
		type: 'userdef'
	},
	{
		id: '123456-2',
		name: 'On treatment',
		type: 'userdef'
	},
	{
		id: '123456-3',
		name: 'Closed cases',
		type: 'warn'
	},
	{
		id: '123456-4',
		name: 'DR-TB with no resistance',
		type: 'danger'
	},
	{
		id: '123456-5',
		name: 'TB with resistance',
		type: 'danger'
	}
];

const followUp = [
	{
		type: 'MEDEXAM',
		name: __('FollowUpType.MEDEXAM'),
		monthOfTreatment: 'Before TB Diagnosis',
		data: {
			id: '123-123-12-3123',
			date: new Date(2016, 1, 1),
			comments: 'Gostaria de enfatizar que o fenômeno da Internet acarreta um processo de reformulação e modernização das novas proposições.',
			weight: 90.00,
			height: 1.80,
			appointmentType: 'SCHEDULLED',
			usingPrescMedicines: 'NO',
			reasonNotUsingPrescMedicines: 'No money for the transportation',
			responsible: 'Mauricio Jesus dos Santos',
			positionResponsible: 'Doctor'
		}
	},
	{
		type: 'MICROSCOPY',
		name: __('FollowUpType.MICROSCOPY'),
		monthOfTreatment: 'Month of Diagnosis',
		data: {
			id: '34-534-5-345',
			// lab exam fields
			dateCollected: new Date(2016, 1, 3),
			sampleNumber: 'MIC020395474',
			comments: 'A prática cotidiana prova que a adoção de políticas descentralizadoras nos obriga à análise dos relacionamentos verticais entre as hierarquias.',
			laboratory: 'da9ca1e1-1c48-11e6-9ef7-4b7adf2d41c0',
			dateRelease: new Date(2016, 3, 1),
			status: 'PERFORMED',
			// Specific fields
			result: 'NEGATIVE',
			numberOfAFB: 5,
			sampleType: 'SPUTUM',
			otherSampleType: null,
			visualAppearance: 'MUCOPURULENT'
		}
	},
	{
		type: 'CULTURE',
		name: __('FollowUpType.CULTURE'),
		monthOfTreatment: '1st month',
		data: {
			id: '334-5-3453-4',
			// lab exam fields
			dateCollected: new Date(2016, 2, 14),
			sampleNumber: 'CUL2342345645',
			comments: 'É importante questionar o quanto o início da atividade geral de formação de atitudes nos obriga à análise do fluxo de informações.',
			laboratory: 'da9ca1e1-1c48-11e6-9ef7-4b7adf2d41c0',
			dateRelease: new Date(2016, 4, 1),
			method: 'METHODCULT2',
			status: 'ONGOING',
			// Specific fields
			result: 'POSITIVE',
			numberOfColonies: 2
		}
	},
	{
		type: 'MEDEXAM',
		name: __('FollowUpType.MEDEXAM'),
		monthOfTreatment: '2nd month',
		data: {
				id: '3-123-123-12-3123',
				date: new Date(2016, 2, 16),
				comments: 'Gostaria de enfatizar que o fenômeno da Internet acarreta um processo de reformulação e modernização das novas proposições.',
				weight: 85.00,
				height: 1.82,
				appointmentType: 'SCHEDULLED',
				usingPrescMedicines: 'NO',
				reasonNotUsingPrescMedicines: null,
				responsible: 'Mauricio Jesus dos Santos',
				positionResponsible: 'Doctor'
		}
	},
	{
		type: 'XPERT',
		name: __('FollowUpType.XPERT'),
		monthOfTreatment: '3rd month',
		data: {
			id: '234-23-4-23',
			// lab exam fields
			dateCollected: new Date(2016, 4, 2),
			sampleNumber: 'XPERT67257',
			comments: 'É importante questionar o quanto o início da atividade geral de formação de atitudes nos obriga à análise do fluxo de informações e de enfatizar que o fenômeno da Internet acarreta um processo de reformulação e modernização',
			laboratory: 'da9ca1e1-1c48-11e6-9ef7-4b7adf2d41c0',
			dateRelease: new Date(2016, 4, 2),
			status: 'PERFORMED',
			// Specific fields
			result: 'TB_NOT_DETECTED',
			rifResult: 'RIF_NOT_DETECTED'
		}
	},
	{
		type: 'MICROSCOPY',
		name: __('FollowUpType.MICROSCOPY'),
		monthOfTreatment: '4th month',
		data: {
			id: '35-34-53-4-3-5',
			// lab exam fields
			dateCollected: new Date(2016, 6, 2),
			sampleNumber: 'MIC020395474',
			comments: 'A prática cotidiana prova que a adoção de políticas descentralizadoras nos obriga à análise dos relacionamentos verticais entre as hierarquias.',
			laboratory: 'da9ca1e1-1c48-11e6-9ef7-4b7adf2d41c0',
			dateRelease: new Date(2016, 6, 2),
			method: 'METHODMIC2',
			status: 'PERFORMED',
			// Specific fields
			result: 'NEGATIVE',
			numberOfAFB: null,
			sampleType: 'OTHER',
			otherSampleType: 'skin',
			visualAppearance: 'SALIVA'
		}
	},
	{
		type: 'DST',
		name: __('FollowUpType.DST'),
		monthOfTreatment: '4th month',
		data: {
				id: '34-5-345-3',
				// lab exam fields
				dateCollected: new Date(2016, 6, 12),
				sampleNumber: 'DST0395474',
				comments: null,
				laboratory: 'da9ca1e1-1c48-11e6-9ef7-4b7adf2d41c0',
				dateRelease: new Date(2016, 25, 3),
				method: 'METHOD2',
				status: 'PERFORMED',
				// Specific fields
				results: [
					{
						substance: '024fadc0-1c4b-11e6-9ef7-4b7adf2d41c0',
						result: 'RESISTANT'
					},
					{
						substance: '024fadc2-1c4b-11e6-9ef7-4b7adf2d41c0',
						result: 'RESISTANT'
					},
					{
						substance: '00bd4df0-1c4b-11e6-9ef7-4b7adf2d41c0',
						result: 'RESISTANT'
					},
					{
						substance: '024fadc4-1c4b-11e6-9ef7-4b7adf2d41c0',
						result: 'RESISTANT'
					},
					{
						substance: '0173f3c0-1c4b-11e6-9ef7-4b7adf2d41c0',
						result: 'RESISTANT'
					}
				]
		}
	},
	{
		type: 'XRAY',
		name: __('FollowUpType.XRAY'),
		monthOfTreatment: '4th month',
		data: {
			id: '546-565',
			date: new Date(2016, 8, 14),
			comments: 'Gostaria de enfatizar que o fenômeno da Internet acarreta um processo de reformulação e modernização das novas proposições.',
			// Specific fields
			evolution: 'PROGRESSED',
			presentation: 'PRESENTATION039'
		}
	},
	{
		type: 'HIV',
		name: __('FollowUpType.HIV'),
		monthOfTreatment: '4th month',
		data: {
			id: '345-345-34-5-345',
			date: new Date(2016, 10, 4),
			comments: 'Gostaria de enfatizar que o fenômeno da Internet acarreta um processo de reformulação e modernização das novas proposições.',
			// Specific fields
			result: 'POSITIVE',
			startedARTdate: null,
			startedCPTdate: new Date(2016, 4, 10),
			laboratory: 'Ana neri'
		}
	}
];


// TEMPORARY -> CASE DATA USED FOR PROTOTYPING
const mockTbCase = {
	patient: {
		name: 'Jim Morrison',
		gender: 'MALE',
		birthDate: new Date(1970, 1, 1),
		motherName: 'Maria Morrison'
	},
	diagnosisDate: new Date(2016, 5, 1),
	age: 35,
	notificationUnit: {
		id: '123456-123355',
		name: 'Centro de Referência Prof Hélio Fraga',
		adminUnit: {
			p1: {
				id: '1234-1234',
				name: 'RJ'
			},
			p2: {
				id: '12333-333',
				name: 'Rio de Janeiro'
			}
		}
	},
	notifAddress: {
		adminUnit: {
			p1: {
				id: '1234-1234',
				name: 'RJ'
			},
			p2: {
				id: '12333-333',
				name: 'Rio de Janeiro'
			}
		}
	},
	tags: tags,
	adverseReactions: [
		{
			id: '4848484-1',
			adverseReaction: { id: 1, name: 'Adverse Reaction 1' },
			medicine: 'Terizidon',
			month: 2
		},
		{
			id: '4848484-2',
			adverseReaction: { id: 2, name: 'Adverse Reaction 2' },
			medicine: 'Isoniazid',
			month: 5
		},
		{
			id: '4848484-3',
			adverseReaction: { id: 1, name: 'Adverse Reaction 3' },
			medicine: 'Amicacin',
			month: 8
		}
	],
	comments: [
		{
			id: '123456-12',
			user: {
				id: '12312312',
				name: 'Bruce Dickinson'
			},
			group: 'contacts',
			date: new Date(),
			comment: 'is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum'
		},
		{
			id: '123456-11',
			user: {
				id: '12312312',
				name: 'Iron Maiden'
			},
			group: 'contacts',
			date: new Date(),
			comment: 'Contact Rubens Smith refused interview and moved out to another address'
		}
	],
	followUp: followUp
};


export { mockTbCase };

export function mockCrud() {
	return new MockCrud();
}

function randonIndex(size) {
	return Math.round(Math.random() * size);
}

const firstNamesMale = [
	'Airton',
	'Augusto',
	'Abu',
	'Abdul',
	'Albert',
	'Antony',
	'Anderson',
	'Arnaldo',
	'Andre',
	'Alan',
	'Alexandre',
	'Alex',
	'Aleksey',
	'Arin',
	'Arnauld',
	'Agnaldo',
	'Albert',
	'Alberto',
	'Abaeté',
	'Abdão',
	'Abdias',
	'Abel',
	'Ademar',
	'Barnabas',
	'Barnaby',
	'Barton',
	'Bernard',
	'Bevis',
	'Bond',
	'Booth',
	'Brick',
	'Brier',
	'Brigham',
	'Brinley',
	'Baltasar',
	'Baren',
	'Beto',
	'Benedito',
	'Bernardo',
	'Benjamin',
	'Becan',
	'Bruno',
	'Brandon',
	'Biafra',
	'Baltazar',
	'Bartolomeu',
	'Batista',
	'Belisario',
	'Benjamin',
	'Bonifácio',
	'Bruce',
	'Carter',
	'Cecil',
	'Cedric',
	'Charles',
	'Chick',
	'Chilton',
	'Chip',
	'Christian',
	'Christopher',
	'Cicero',
	'Cláudio',
	'Clenildo',
	'Carlos',
	'Caetano',
	'Clayton',
	'Claude',
	'Conrado',
	'Constantino',
	'Constantin',
	'Caio',
	'Clarence',
	'Clay',
	'Clayland',
	'Clayton',
	'Cleave',
	'Clem',
	'Clemens',
	'Clement',
	'Cliff',
	'Clifford',
	'Clint',
	'Clinton',
	'Dado',
	'Daniel',
	'Dilson',
	'David',
	'Dean',
	'Dean',
	'Diamond',
	'Dixon',
	'Duff',
	'Ebenezer',
	'Edgar',
	'Edward',
	'Edwin',
	'Eliah',
	'Eliezer',
	'Iuri',
	'Yuri',
	'Jean',
	'Mohamed',
	'Nuno',
	'Norberto',
	'Nilo',
	'Nicholas',
	'Noel',
	'Osmar',
	'Oswald',
	'Paulo',
	'Pedro',
	'Peter',
	'Paul',
	'Phillip',
	'Patrick',
	'Raymond',
	'Ricardo',
	'Roosevelt',
	'Rennan',
	'Renato',
	'Raul',
	'Ramon',
	'Rachid',
	'Ringo',
	'Sergey',
	'Samuel',
	'Sandro',
	'Silvester',
	'Steffano',
	'Steve',
	'Will',
	'Xavier'
];

const firstNamesFemale = [
	'Alynn',
	'Abbey',
	'Aaron',
	'Abbie',
	'Abbigail',
	'Abby',
	'Abigail',
	'Abigayle',
	'Abilene',
	'Abiranna',
	'Ada',
	'Adabel',
	'Addison',
	'Addsyn',
	'Adela',
	'Albertina',
	'Agnes',
	'Andreia',
	'Angela',
	'Arinan',
	'Ana Maria',
	'Ana Lúcia',
	'Ana',
	'Ameely',
	'Alice',
	'Aline',
	'Arminda',
	'Benedita',
	'Bruna',
	'Beatriz',
	'Barbara',
	'Bianca',
	'Berenice',
	'Bruna',
	'Clara',
	'Célia',
	'Carolina',
	'Carol',
	'Carla',
	'Clarice',
	'Daniele',
	'Diana',
	'Débora',
	'Emily',
	'Esmeralda',
	'Elisabeth',
	'Flávia',
	'Fátima',
	'Fabiana',
	'Fernanda',
	'Gilda',
	'Helena',
	'Iraci',
	'Iná',
	'Jocilaine',
	'Maria',
	'Maria Antônia',
	'Manoela',
	'Michele',
	'Mel',
	'Melissa',
	'Nair',
	'Karla',
	'Keli',
	'Kimberly',
	'Keila',
	'Lara',
	'Laura',
	'Margareth',
	'Mabel',
	'Madaline',
	'Madelene',
	'Madeline',
	'Madelyn',
	'Nataly',
	'Noelma',
	'Otaviana',
	'Paula',
	'Patrícia',
	'Paloma',
	'Paris',
	'Regina',
	'Roberta',
	'Rogéria',
	'Raquel',
	'Rebeca',
	'Raimunda',
	'Romana',
	'Rose',
	'RaDonna',
	'Rae',
	'Raechel',
	'Sylvia',
	'Sara',
	'Telma',
	'Tara',
	'Uma',
	'Vada',
	'Val',
	'Valarie',
	'Velma',
	'Valéria',
	'Velma',
	'Vanity',
	'Velia',
	'Velma',
	'Quilma',
	'Qbilah',
	'Queen',
	'Queena',
	'Queenie',
	'Quentina',
	'Quiana',
	'Quinn',
	'Karen',
	'Kelly'
];

const lastNames = [
	'Antunes',
	'Abbott',
	'Adamczyk',
	'Agnaudo',
	'Araujo',
	'Apple',
	'da Silva',
	'Debrieux',
	'Batista',
	'Bolivar',
	'Brasil',
	'Bottino',
	'Blue',
	'Bachman',
	'Bailey',
	'Brick',
	'Dianno',
	'DAvona',
	'Dmarco',
	'Da neen',
	'Da wan',
	'Daava',
	'Dafne',
	'Dagian',
	'Dahlia',
	'Daija',
	'Dantas',
	'Dickinson',
	'da Costa',
	'Copala',
	'Cortez',
	'Costeau',
	'da Costa Saldanha',
	'Caliva',
	'Callaghan',
	'Dutra',
	'Boon',
	'Bernardes',
	'Busht',
	'Dabrowski',
	'Daecher',
	'Edwards',
	'Eisenhower',
	'Ford',
	'Farias',
	'Fernandes',
	'Fernandez',
	'Forest',
	'Fancher',
	'Fantauzzo',
	'Gallet',
	'Gomes',
	'Gump',
	'Greenhill',
	'Greece',
	'Green',
	'Gray',
	'Garcia',
	'Gusmão',
	'Gabriell',
	'Gao',
	'Gardenia',
	'Galena',
	'Gallo',
	'Garcia',
	'Hahn',
	'Haley',
	'Himan',
	'Hill',
	'Hutson',
	'Highway',
	'Julia',
	'Jordan',
	'Jackson',
	'Johnson',
	'Silveira'
];


/**
 * Mock class to simulate a crud object
 */
class MockCrud {
	on(func) {
		this.handler = func;
		return this;
	}

	_event(evt, data, defaultRes) {
		return new Promise(resolve => {
			const res = this.handler(evt, data);
			setTimeout(() => {
				if (res) {
					resolve(res);
				} else {
					resolve(defaultRes);
				}
			}, 500);
		});
	}

	get(id) {
		return this._event('get', id);
	}

	getEdit(id) {
		return this._event('get-edit', id);
	}

	create(req) {
		return this._event('create', req, { success: true, result: '123456' });
	}

	update(id) {
		return this._event('update', id, { success: true, result: id });
	}

	query(qry) {
		return this._event('query', qry);
	}

	delete(id) {
		return this._event('delete', id, { success: true, result: id });
	}
}
