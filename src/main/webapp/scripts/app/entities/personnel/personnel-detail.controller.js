'use strict';

angular.module('cmsApp')
    .controller('PersonnelDetailController', function ($scope, $rootScope,$location, $stateParams, entity, Personnel, Consultation, Conjoint, Enfant,Patient) {
        $scope.personnel = entity;
        $scope.patient = {};
        $scope.consultation = {};
        $scope.load = function (id) {
            Personnel.get({id: id}, function(result) {
                $scope.personnel = result;
            });
        };
        $scope.newPatient = function(){
            $scope.patient.nom = $scope.personnel.nom;
            $scope.patient.prenom = $scope.personnel.prenom;
            Patient.save($scope.patient, function(result){
                $scope.consultation.personel = result.id;
                Consultation.save($scope.consultation, function(resultTwo){
                    $location.path("consultation.detail({resultTwo.id})");

                });

            }) ;
            Consultation.save($scope.consultation);
        };

        $rootScope.$on('cmsApp:personnelUpdate', function(event, result) {
            $scope.personnel = result;
        });
    });
