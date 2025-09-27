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

describe('Pago e2e test', () => {
  const pagoPageUrl = '/pago';
  const pagoPageUrlPattern = new RegExp('/pago(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pagoSample = { fechaPago: '2025-09-26', monto: 9900.06, metodoPago: 'TARJETA' };

  let pago;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pagos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pagos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pagos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pago) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pagos/${pago.id}`,
      }).then(() => {
        pago = undefined;
      });
    }
  });

  it('Pagos menu should load Pagos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pago');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pago').should('exist');
    cy.url().should('match', pagoPageUrlPattern);
  });

  describe('Pago page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pagoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pago page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pago/new$'));
        cy.getEntityCreateUpdateHeading('Pago');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pagoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pagos',
          body: pagoSample,
        }).then(({ body }) => {
          pago = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pagos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pagos?page=0&size=20>; rel="last",<http://localhost/api/pagos?page=0&size=20>; rel="first"',
              },
              body: [pago],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pagoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pago page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pago');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pagoPageUrlPattern);
      });

      it('edit button click should load edit Pago page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pago');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pagoPageUrlPattern);
      });

      it('edit button click should load edit Pago page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pago');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pagoPageUrlPattern);
      });

      it('last delete button click should delete instance of Pago', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pago').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pagoPageUrlPattern);

        pago = undefined;
      });
    });
  });

  describe('new Pago page', () => {
    beforeEach(() => {
      cy.visit(`${pagoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pago');
    });

    it('should create an instance of Pago', () => {
      cy.get(`[data-cy="fechaPago"]`).type('2025-09-26');
      cy.get(`[data-cy="fechaPago"]`).blur();
      cy.get(`[data-cy="fechaPago"]`).should('have.value', '2025-09-26');

      cy.get(`[data-cy="monto"]`).type('4398.26');
      cy.get(`[data-cy="monto"]`).should('have.value', '4398.26');

      cy.get(`[data-cy="metodoPago"]`).select('TARJETA');

      cy.get(`[data-cy="referencia"]`).type('joyously vivaciously');
      cy.get(`[data-cy="referencia"]`).should('have.value', 'joyously vivaciously');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pago = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pagoPageUrlPattern);
    });
  });
});
