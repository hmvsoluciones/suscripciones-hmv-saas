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

describe('Suscripcion e2e test', () => {
  const suscripcionPageUrl = '/suscripcion';
  const suscripcionPageUrlPattern = new RegExp('/suscripcion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const suscripcionSample = { fechaInicio: '2025-09-26', fechaFin: '2025-09-26', estado: 'VENCIDA' };

  let suscripcion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/suscripcions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/suscripcions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/suscripcions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (suscripcion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/suscripcions/${suscripcion.id}`,
      }).then(() => {
        suscripcion = undefined;
      });
    }
  });

  it('Suscripcions menu should load Suscripcions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('suscripcion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Suscripcion').should('exist');
    cy.url().should('match', suscripcionPageUrlPattern);
  });

  describe('Suscripcion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(suscripcionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Suscripcion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/suscripcion/new$'));
        cy.getEntityCreateUpdateHeading('Suscripcion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suscripcionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/suscripcions',
          body: suscripcionSample,
        }).then(({ body }) => {
          suscripcion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/suscripcions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/suscripcions?page=0&size=20>; rel="last",<http://localhost/api/suscripcions?page=0&size=20>; rel="first"',
              },
              body: [suscripcion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(suscripcionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Suscripcion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('suscripcion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suscripcionPageUrlPattern);
      });

      it('edit button click should load edit Suscripcion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Suscripcion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suscripcionPageUrlPattern);
      });

      it('edit button click should load edit Suscripcion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Suscripcion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suscripcionPageUrlPattern);
      });

      it('last delete button click should delete instance of Suscripcion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('suscripcion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suscripcionPageUrlPattern);

        suscripcion = undefined;
      });
    });
  });

  describe('new Suscripcion page', () => {
    beforeEach(() => {
      cy.visit(`${suscripcionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Suscripcion');
    });

    it('should create an instance of Suscripcion', () => {
      cy.get(`[data-cy="fechaInicio"]`).type('2025-09-26');
      cy.get(`[data-cy="fechaInicio"]`).blur();
      cy.get(`[data-cy="fechaInicio"]`).should('have.value', '2025-09-26');

      cy.get(`[data-cy="fechaFin"]`).type('2025-09-26');
      cy.get(`[data-cy="fechaFin"]`).blur();
      cy.get(`[data-cy="fechaFin"]`).should('have.value', '2025-09-26');

      cy.get(`[data-cy="estado"]`).select('CANCELADA');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        suscripcion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', suscripcionPageUrlPattern);
    });
  });
});
