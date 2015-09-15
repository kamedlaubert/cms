'use strict';

angular.module('cmsApp').controller('ConsultationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Consultation', 'Practiciens', 'Ordonnance', 'Symptome', 'Hospitalisation', 'Examens', 'Personnel',
        function($scope, $stateParams, $modalInstance, entity, Consultation, Practiciens, Ordonnance, Symptome, Hospitalisation, Examens, Personnel) {

        $scope.consultation = entity;
        $scope.practicienss = Practiciens.query();
        $scope.ordonnances = Ordonnance.query();
        $scope.symptomes = Symptome.query();
        $scope.hospitalisations = Hospitalisation.query();
        $scope.examenss = Examens.query();
        $scope.personnels = Personnel.query();
        $scope.load = function(id) {
            Consultation.get({id : id}, function(result) {
                $scope.consultation = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:consultationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.consultation.id != null) {
                Consultation.update($scope.consultation, onSaveFinished);
            } else {
                Consultation.save($scope.consultation, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
