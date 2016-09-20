class HelloWorld extends HTMLElement {

	static get observedAttributes() {
		return ['message'];
	}

	constructor() {
		super();
		let template = document.querySelector('#hello-world-template').content.cloneNode(true);
		let root = this.attachShadow({mode: 'open'});
		root.appendChild(template);
	}

	get message() {
		return this.hasAttribute('message');
	}

	set message(message) {
		if (message) {
			this.setAttribute('message', message);
		} else {
			this.removeAttribute('message');
		}
	}

	/** Fires when an instance was inserted into the document */
	connectedCallback() {

	};

	/** Fires when an instance was removed from the document */
	disconnectedCallback() {
	};

	/** Fires when an attribute was added, removed, or updated */
	attributeChangedCallback(attr, oldVal, newVal) {
		if (attr === 'message') {
			let messageElement = this.shadowRoot.querySelector('#message');
			messageElement.innerText = newVal;
		}
	}

	/** Fires when the element is moved to a new document */
	adoptedCallback() {
	}
}
window.customElements.define('hello-world', HelloWorld);
