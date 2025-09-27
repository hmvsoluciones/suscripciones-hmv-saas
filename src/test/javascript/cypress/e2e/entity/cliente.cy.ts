import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Cliente e2e test', () => {
  const clientePageUrl = '/cliente';
  const clientePageUrlPattern = new RegExp('/cliente(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const clienteSample = { nombre: 'willing', email: 'Pablo.MontanoFajardo81@yahoo.com' };

  let cliente;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/clientes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/clientes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/clientes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cliente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/clientes/${cliente.id}`,
      }).then(() => {
        cliente = undefined;
      });
    }
  });

  it('Clientes menu should load Clientes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cliente');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cliente').should('exist');
    cy.url().should('match', clientePageUrlPattern);
  });

  describe('Cliente page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(clientePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cliente page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cliente/new$'));
        cy.getEntityCreateUpdateHeading('Cliente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/clientes',
          body: clienteSample,
        }).then(({ body }) => {
          cliente = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/clientes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/clientes?page=0&size=20>; rel="last",<http://localhost/api/clientes?page=0&size=20>; rel="first"',
              },
              body: [cliente],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(clientePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cliente page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cliente');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientePageUrlPattern);
      });

      it('edit button click should load edit Cliente page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cliente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientePageUrlPattern);
      });

      it('edit button click should load edit Cliente page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cliente');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientePageUrlPattern);
      });

      it('last delete button click should delete instance of Cliente', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cliente').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', clientePageUrlPattern);

        cliente = undefined;
      });
    });
  });

  describe('new Cliente page', () => {
    beforeEach(() => {
      cy.visit(`${clientePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cliente');
    });

    it('should create an instance of Cliente', () => {
      cy.get(`[data-cy="nombre"]`).type('scarcely');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'scarcely');

      cy.get(`[data-cy="email"]`).type('Patricio.GalindoMunoz@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Patricio.GalindoMunoz@gmail.com');

      cy.get(`[data-cy="telefono"]`).type('muddy incidentally');
      cy.get(`[data-cy="telefono"]`).should('have.value', 'muddy incidentally');

      cy.get(`[data-cy="razonSocial"]`).type('and menacing promise');
      cy.get(`[data-cy="razonSocial"]`).should('have.value', 'and menacing promise');

      cy.get(`[data-cy="rfc"]`).type('eek meaningfu');
      cy.get(`[data-cy="rfc"]`).should('have.value', 'eek meaningfu');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cliente = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', clientePageUrlPattern);
    });
  });
});
