'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('personnel', {
                parent: 'entity',
                url: '/personnels',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.personnel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/personnel/personnels.html',
                        controller: 'PersonnelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('personnel');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('personnel.detail', {
                parent: 'entity',
                url: '/personnel/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.personnel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/personnel/personnel-detail.html',
                        controller: 'PersonnelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('personnel');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Personnel', function($stateParams, Personnel) {
                        return Personnel.get({id : $stateParams.id});
                    }]
                }
            })
            .state('personnel.new', {
                parent: 'personnel',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/personnel/personnel-dialog.html',
                        controller: 'PersonnelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {matricule: null, nom: null, prenom: null, direction: null, age: null, sexe: null, type: null, famille: null, groupeSanguin: null, infosCompl: null, allergie: null, statut: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('personnel', null, { reload: true });
                    }, function() {
                        $state.go('personnel');
                    })
                }]
            })
            .state('personnel.edit', {
                parent: 'personnel',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/personnel/personnel-dialog.html',
                        controller: 'PersonnelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Personnel', function(Personnel) {
                                return Personnel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('personnel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
