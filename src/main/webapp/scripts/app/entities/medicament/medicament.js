'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('medicament', {
                parent: 'entity',
                url: '/medicaments',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.medicament.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/medicament/medicaments.html',
                        controller: 'MedicamentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('medicament');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('medicament.detail', {
                parent: 'entity',
                url: '/medicament/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.medicament.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/medicament/medicament-detail.html',
                        controller: 'MedicamentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('medicament');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Medicament', function($stateParams, Medicament) {
                        return Medicament.get({id : $stateParams.id});
                    }]
                }
            })
            .state('medicament.new', {
                parent: 'medicament',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/medicament/medicament-dialog.html',
                        controller: 'MedicamentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nom: null, posologie: null, indParticulier: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('medicament', null, { reload: true });
                    }, function() {
                        $state.go('medicament');
                    })
                }]
            })
            .state('medicament.edit', {
                parent: 'medicament',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/medicament/medicament-dialog.html',
                        controller: 'MedicamentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Medicament', function(Medicament) {
                                return Medicament.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('medicament', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
