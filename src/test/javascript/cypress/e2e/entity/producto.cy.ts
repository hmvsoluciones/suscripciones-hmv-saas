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

describe('Producto e2e test', () => {
  const productoPageUrl = '/producto';
  const productoPageUrlPattern = new RegExp('/producto(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productoSample = { nombre: 'machine gleefully shadowbox', descripcion: 'to brr' };

  let producto;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/productos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/productos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/productos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (producto) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/productos/${producto.id}`,
      }).then(() => {
        producto = undefined;
      });
    }
  });

  it('Productos menu should load Productos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('producto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Producto').should('exist');
    cy.url().should('match', productoPageUrlPattern);
  });

  describe('Producto page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Producto page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/producto/new$'));
        cy.getEntityCreateUpdateHeading('Producto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/productos',
          body: productoSample,
        }).then(({ body }) => {
          producto = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/productos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/productos?page=0&size=20>; rel="last",<http://localhost/api/productos?page=0&size=20>; rel="first"',
              },
              body: [producto],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Producto page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('producto');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productoPageUrlPattern);
      });

      it('edit button click should load edit Producto page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Producto');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productoPageUrlPattern);
      });

      it('edit button click should load edit Producto page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Producto');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productoPageUrlPattern);
      });

      it('last delete button click should delete instance of Producto', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('producto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productoPageUrlPattern);

        producto = undefined;
      });
    });
  });

  describe('new Producto page', () => {
    beforeEach(() => {
      cy.visit(`${productoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Producto');
    });

    it('should create an instance of Producto', () => {
      cy.get(`[data-cy="nombre"]`).type('even amongst cycle');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'even amongst cycle');

      cy.get(`[data-cy="descripcion"]`).type('since');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'since');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click();
      cy.get(`[data-cy="activo"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        producto = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productoPageUrlPattern);
    });
  });
});
