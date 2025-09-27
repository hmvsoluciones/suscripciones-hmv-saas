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

describe('TipoProducto e2e test', () => {
  const tipoProductoPageUrl = '/tipo-producto';
  const tipoProductoPageUrlPattern = new RegExp('/tipo-producto(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoProductoSample = { nombre: 'overfeed coolly' };

  let tipoProducto;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-productos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-productos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-productos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoProducto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-productos/${tipoProducto.id}`,
      }).then(() => {
        tipoProducto = undefined;
      });
    }
  });

  it('TipoProductos menu should load TipoProductos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-producto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoProducto').should('exist');
    cy.url().should('match', tipoProductoPageUrlPattern);
  });

  describe('TipoProducto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoProductoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoProducto page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo-producto/new$'));
        cy.getEntityCreateUpdateHeading('TipoProducto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoProductoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-productos',
          body: tipoProductoSample,
        }).then(({ body }) => {
          tipoProducto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-productos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tipo-productos?page=0&size=20>; rel="last",<http://localhost/api/tipo-productos?page=0&size=20>; rel="first"',
              },
              body: [tipoProducto],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoProductoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoProducto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoProducto');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoProductoPageUrlPattern);
      });

      it('edit button click should load edit TipoProducto page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoProducto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoProductoPageUrlPattern);
      });

      it('edit button click should load edit TipoProducto page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoProducto');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoProductoPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoProducto', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoProducto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoProductoPageUrlPattern);

        tipoProducto = undefined;
      });
    });
  });

  describe('new TipoProducto page', () => {
    beforeEach(() => {
      cy.visit(`${tipoProductoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TipoProducto');
    });

    it('should create an instance of TipoProducto', () => {
      cy.get(`[data-cy="nombre"]`).type('excluding');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'excluding');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        tipoProducto = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoProductoPageUrlPattern);
    });
  });
});
