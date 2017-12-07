//
//  ViewController.swift
//  CourseTracker
//
//  Created by Diego  Bustamante on 10/1/17.
//  Copyright © 2017 Diego Bustamante. All rights reserved.
//
import Foundation
import UIKit

class ViewController: UIViewController {

    // Figure out what weak var is..
    
    @IBOutlet weak var UserNameTextField: UITextField!
    @IBOutlet weak var UserPasswordTextField: UITextField!
    var uuuid: String!
    
    // On login click, this function is activated
    @IBAction func LoginPress(_ sender: Any) {
        print("Login works")
        self.uuuid = ""
        let userName = UserNameTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        let userPassword = UserPasswordTextField.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        print("Here in checkLogin")
        
        // Check for empty fields
        if((userName?.isEmpty)! || (userPassword?.isEmpty)!){
            // display alert message
            displayMyAlertMessage(UserMessage: "purequired")
            return
        }
        
        checkLogin(userName!, userPassword!)
        
        
    }
    
    // checks login credentials are correct
    func checkLogin(_ user: String, _ pw: String){
        let group = DispatchGroup()
        group.enter()
        // verify data
        let url = URL(string: "https://coursetrackerumw.herokuapp.com/users?user=" + user + "&pw=" + pw)
        DispatchQueue.main.async {
        let task = URLSession.shared.dataTask(with: url!) {(data, response, error) in
            if error != nil
            {
                print("ERROR")
            }
            else{
                print("Here in checkLogin 2")
                    if let content = data
                    {
                        do {
                            print("Here in checkLogin 3")
                            let myJson = try JSONSerialization.jsonObject(with: content, options: .mutableContainers)
                            print(myJson as AnyObject)
                            for dictionary in myJson as! [[String: AnyObject]]{
                                print("Here")
                                print(dictionary["_id"] as AnyObject)
                                print(dictionary["name"] as AnyObject)
                                self.uuuid = dictionary["uid"]! as! String
                                print("UUID: " + self.uuuid as String!)
                                group.leave()
                            }
        
                        }
                        catch
                        {
                            return
                        }
                    }
                    else
                    {
                        
                        return
                    }
                }
            }
            task.resume()
        }
            group.notify(queue: .main) {
            if self.uuuid != ""{
                self.performSegue(withIdentifier: "loggedin", sender: self)
            }
            else{
                self.displayMyAlertMessage(UserMessage: "Username or Password is incorrect. Please Try Again")
            }
            }
        }
    
    // Passes Variables through segue
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let secondController = segue.destination as! ViewTwo
        secondController.myUUID = self.uuuid
        
    }
    
    
    // displays the popup for message errors
    func displayMyAlertMessage(UserMessage: String){
        let myAlert = UIAlertController(title:"Alert", message: UserMessage, preferredStyle: UIAlertControllerStyle.alert);
        myAlert.addAction(UIAlertAction(title: "Dismiss", style: UIAlertActionStyle.default,handler: nil))
        self.present(myAlert, animated: true, completion: nil);
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

