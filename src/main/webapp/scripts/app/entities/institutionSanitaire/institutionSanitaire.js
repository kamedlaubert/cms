'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('institutionSanitaire', {
                parent: 'entity',
                url: '/institutionSanitaires',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.institutionSanitaire.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/institutionSanitaire/institutionSanitaires.html',
                        controller: 'InstitutionSanitaireController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('institutionSanitaire');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('institutionSanitaire.detail', {
                parent: 'entity',
                url: '/institutionSanitaire/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.institutionSanitaire.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/institutionSanitaire/institutionSanitaire-detail.html',
                        controller: 'InstitutionSanitaireDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('institutionSanitaire');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'InstitutionSanitaire', function($stateParams, InstitutionSanitaire) {
                        return InstitutionSanitaire.get({id : $stateParams.id});
                    }]
                }
            })
            .state('institutionSanitaire.new', {
                parent: 'institutionSanitaire',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/institutionSanitaire/institutionSanitaire-dialog.html',
                        controller: 'InstitutionSanitaireDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {raisonSocial: null, nom: null, type: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('institutionSanitaire', null, { reload: true });
                    }, function() {
                        $state.go('institutionSanitaire');
                    })
                }]
            })
            .state('institutionSanitaire.edit', {
                parent: 'institutionSanitaire',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/institutionSanitaire/institutionSanitaire-dialog.html',
                        controller: 'InstitutionSanitaireDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['InstitutionSanitaire', function(InstitutionSanitaire) {
                                return InstitutionSanitaire.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('institutionSanitaire', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
