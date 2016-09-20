class ContactCard extends HTMLElement {

	constructor() {
		super();
		let template = document.querySelector('#contact-card-template').content.cloneNode(true);
		let root = this.attachShadow({mode: 'open'});
		root.appendChild(template);
		root.querySelector('#test').addEventListener('click', () => {
			alert('hi')
		});
	}

	/** Fires when an instance was inserted into the document */
	connectedCallback() {

	};

	/** Fires when an instance was removed from the document */
	disconnectedCallback() {
		this.stop();
	};

	/** Fires when an attribute was added, removed, or updated */
	attributeChangedCallback(attr, oldVal, newVal) {
		if (attr == "start") {
			this.count = Number(newVal);
		}
	}

	/** Fires when the element is moved to a new document */
	adoptedCallback() {
		console.log("Someone adopted me!", this);
	}
}
window.customElements.define('contact-card', ContactCard);
